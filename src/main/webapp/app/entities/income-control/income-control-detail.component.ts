import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIncomeControl } from 'app/shared/model/income-control.model';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-income-control-detail',
  templateUrl: './income-control-detail.component.html'
})
export class IncomeControlDetailComponent implements OnInit {
  incomeControl: IIncomeControl | null = null;
  public imageBase64: any = null;

  constructor(protected domSanitizer: DomSanitizer, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeControl }) => {
      this.incomeControl = incomeControl;
      if (this.incomeControl) {
        if (this.incomeControl.visitor) {
          this.imageBase64 = this.incomeControl.visitor.imageName
            ? this.domSanitizer.bypassSecurityTrustUrl(this.incomeControl.visitor.imageName)
            : null;
        }
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
