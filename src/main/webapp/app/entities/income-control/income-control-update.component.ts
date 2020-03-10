import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IIncomeControl, IncomeControl, IncomeControlDTO } from 'app/shared/model/income-control.model';
import { IncomeControlService } from './income-control.service';
import { IVisitor, Visitor } from 'app/shared/model/visitor.model';
import { VisitorService } from 'app/entities/visitor/visitor.service';
import { WebcamImage, WebcamInitError, WebcamUtil } from 'ngx-webcam';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/document-type.service';

@Component({
  selector: 'jhi-income-control-update',
  templateUrl: './income-control-update.component.html'
})
export class IncomeControlUpdateComponent implements OnInit {
  isSaving = false;
  documenttypes: IDocumentType[] = [];

  visitor: IVisitor = new Visitor();
  editForm = this.fb.group({
    id: [],

    fullName: [null, [Validators.required]],
    documentNumber: [null, [Validators.required]],
    business: [],
    position: [],
    documentType: [null, Validators.required],

    reason: [null, [Validators.required]],
    place: [null, [Validators.required]],
    answerable: [null, [Validators.required]]
  });
  //web configure
  // toggle webcam on/off
  public showWebcam = true;
  public allowCameraSwitch = true;
  public multipleWebcamsAvailable = false;
  public deviceId: string = '';
  public videoOptions: MediaTrackConstraints = {
    width: { ideal: 350 },
    height: { ideal: 350 }
  };
  public errors: WebcamInitError[] = [];

  // latest snapshot
  public webcamImage: any = null; //WebcamImage
  public imageBase64: any = null;
  public image: any = null;

  // webcam snapshot trigger
  private trigger: Subject<void> = new Subject<void>();
  // switch to next / previous / specific webcam; true/false: forward/backwards, string: deviceId
  private nextWebcam: Subject<boolean | string> = new Subject<boolean | string>();

  constructor(
    protected domSanitizer: DomSanitizer,
    protected documentTypeService: DocumentTypeService,
    protected incomeControlService: IncomeControlService,
    protected visitorService: VisitorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeControl }) => {
      if (!incomeControl.id) {
        const today = moment().startOf('day');
        incomeControl.admissionDate = today;
        incomeControl.departureDate = today;
      }

      this.updateForm(incomeControl);

      this.documentTypeService.query().subscribe((res: HttpResponse<IDocumentType[]>) => (this.documenttypes = res.body || []));
    });

    this.onChanges();
  }

  updateForm(incomeControl: IIncomeControl): void {
    this.editForm.patchValue({
      id: incomeControl.id,

      fullName: incomeControl.visitor ? incomeControl.visitor.fullName : null,
      documentNumber: incomeControl.visitor ? incomeControl.visitor.documentNumber : null,
      business: incomeControl.visitor ? incomeControl.visitor.business : null,
      position: incomeControl.visitor ? incomeControl.visitor.position : null,
      documentType: incomeControl.visitor ? incomeControl.visitor.documentType : null,

      reason: incomeControl.reason,
      place: incomeControl.place,
      answerable: incomeControl.answerable
    });
    if (incomeControl.visitor) {
      this.visitor = incomeControl.visitor;
      this.imageBase64 = incomeControl.visitor.imageName ? this.domSanitizer.bypassSecurityTrustUrl(incomeControl.visitor.imageName) : null;
      this.image = incomeControl.visitor.imageName;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incomeControl = this.createFromForm();
    if (incomeControl.id !== undefined) {
      this.subscribeToSaveResponse(this.incomeControlService.update(incomeControl));
    } else {
      this.subscribeToSaveResponse(this.incomeControlService.create(incomeControl));
    }
  }

  onChanges(): void {
    this.editForm.get(['documentNumber'])!.valueChanges.subscribe(value => {
      let documentNumber: string = value;
      let documentType: IDocumentType = this.editForm.get(['documentType'])!.value;
      if (documentNumber && documentType) {
        this.loadVisitor(documentNumber, documentType);
      }
    });

    this.editForm.get(['documentType'])!.valueChanges.subscribe(value => {
      let documentNumber = this.editForm.get(['documentNumber'])!.value;
      let documentType: IDocumentType = value;
      if (documentNumber && documentType) {
        this.loadVisitor(documentNumber, documentType);
      }
    });
  }

  loadVisitor(documentNumber: string, documentType: IDocumentType) {
    if (documentNumber && documentType.id != null) {
      this.visitorService.findDocumentNumberAndDocumentType(documentNumber, documentType.id).subscribe(
        value => {
          if (!value.body) {
            this.visitor = new Visitor();
          } else {
            if (!this.visitor.id) {
              this.visitor = value.body;
              this.updateFormVisitor(this.visitor);
            } else {
              if (!(value.body.id === this.visitor.id)) {
                this.visitor = value.body;
                this.updateFormVisitor(this.visitor);
              }
            }
          }
        },
        error => {
          this.visitor = new Visitor();
        }
      );
    }
  }

  updateFormVisitor(visitor: IVisitor): void {
    if (visitor) {
      this.editForm.patchValue({
        fullName: visitor ? visitor.fullName : null,
        documentNumber: visitor ? visitor.documentNumber : null,
        business: visitor ? visitor.business : null,
        position: visitor ? visitor.position : null,
        documentType: visitor ? visitor.documentType : null
      });

      this.visitor = visitor;
      this.imageBase64 = visitor.imageName ? this.domSanitizer.bypassSecurityTrustUrl(visitor.imageName) : null;
      this.image = visitor.imageName;
    }
  }

  private createFromForm(): IIncomeControl {
    this.visitor.fullName = this.editForm.get(['fullName'])!.value;
    this.visitor.documentNumber = this.editForm.get(['documentNumber'])!.value;
    this.visitor.business = this.editForm.get(['business'])!.value;
    this.visitor.position = this.editForm.get(['position'])!.value;
    this.visitor.documentType = this.editForm.get(['documentType'])!.value;
    if (this.imageBase64) {
      this.visitor.imageName = this.image;
    }

    return {
      ...new IncomeControlDTO(),
      id: this.editForm.get(['id'])!.value,
      reason: this.editForm.get(['reason'])!.value,
      place: this.editForm.get(['place'])!.value,
      answerable: this.editForm.get(['answerable'])!.value,
      visitor: this.visitor
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncomeControl>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IDocumentType): any {
    return item.id;
  }

  public triggerSnapshot(): void {
    this.trigger.next();
  }

  public newPhoto(): void {
    this.webcamImage = null;
    this.imageBase64 = null;
    this.image = null;
  }

  public toggleWebcam(): void {
    this.showWebcam = !this.showWebcam;
  }

  public handleInitError(error: WebcamInitError): void {
    this.errors.push(error);
  }

  public handleImage(webcamImage: WebcamImage): void {
    console.info('received webcam image', webcamImage);
    this.webcamImage = webcamImage;
    this.imageBase64 = this.webcamImage.imageAsDataUrl;
    this.image = this.webcamImage.imageAsDataUrl;
  }

  public cameraWasSwitched(deviceId: string): void {
    console.log('active device: ' + deviceId);
    this.deviceId = deviceId;
  }

  public get triggerObservable(): Observable<void> {
    return this.trigger.asObservable();
  }

  public get nextWebcamObservable(): Observable<boolean | string> {
    return this.nextWebcam.asObservable();
  }
}
