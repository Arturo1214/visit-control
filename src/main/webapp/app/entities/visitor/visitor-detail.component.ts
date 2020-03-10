import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVisitor } from 'app/shared/model/visitor.model';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-visitor-detail',
  templateUrl: './visitor-detail.component.html'
})
export class VisitorDetailComponent implements OnInit {
  visitor: IVisitor | null = null;
  public imageBase64: any = null;

  constructor(protected domSanitizer: DomSanitizer, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visitor }) => {
      this.visitor = visitor;
      this.imageBase64 = visitor.imageName ? this.domSanitizer.bypassSecurityTrustUrl(visitor.imageName) : null;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
