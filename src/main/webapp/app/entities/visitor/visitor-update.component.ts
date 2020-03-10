import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject } from 'rxjs';

import { IVisitor, Visitor } from 'app/shared/model/visitor.model';
import { VisitorService } from './visitor.service';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/document-type.service';
import { WebcamImage, WebcamInitError, WebcamUtil } from 'ngx-webcam';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-visitor-update',
  templateUrl: './visitor-update.component.html'
})
export class VisitorUpdateComponent implements OnInit {
  isSaving = false;
  documenttypes: IDocumentType[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required]],
    documentNumber: [null, [Validators.required]],
    business: [],
    position: [],
    documentType: [null, Validators.required]
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
    protected visitorService: VisitorService,
    protected documentTypeService: DocumentTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visitor }) => {
      this.updateForm(visitor);

      this.documentTypeService.query().subscribe((res: HttpResponse<IDocumentType[]>) => (this.documenttypes = res.body || []));
    });

    WebcamUtil.getAvailableVideoInputs().then((mediaDevices: MediaDeviceInfo[]) => {
      this.multipleWebcamsAvailable = mediaDevices && mediaDevices.length > 1;
    });
  }

  updateForm(visitor: IVisitor): void {
    this.editForm.patchValue({
      id: visitor.id,
      fullName: visitor.fullName,
      documentNumber: visitor.documentNumber,
      business: visitor.business,
      position: visitor.position,
      documentType: visitor.documentType
    });
    this.imageBase64 = visitor.imageName ? this.domSanitizer.bypassSecurityTrustUrl(visitor.imageName) : null;
    this.image = visitor.imageName;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visitor = this.createFromForm();
    if (visitor.id !== undefined) {
      this.subscribeToSaveResponse(this.visitorService.update(visitor));
    } else {
      this.subscribeToSaveResponse(this.visitorService.create(visitor));
    }
  }

  private createFromForm(): IVisitor {
    return {
      ...new Visitor(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      documentNumber: this.editForm.get(['documentNumber'])!.value,
      business: this.editForm.get(['business'])!.value,
      position: this.editForm.get(['position'])!.value,
      imageName: this.image,
      documentType: this.editForm.get(['documentType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisitor>>): void {
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
    console.error(error);
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
