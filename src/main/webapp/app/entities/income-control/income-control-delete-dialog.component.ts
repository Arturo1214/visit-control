import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIncomeControl } from 'app/shared/model/income-control.model';
import { IncomeControlService } from './income-control.service';

@Component({
  templateUrl: './income-control-delete-dialog.component.html'
})
export class IncomeControlDeleteDialogComponent {
  incomeControl?: IIncomeControl;

  constructor(
    protected incomeControlService: IncomeControlService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incomeControlService.delete(id).subscribe(() => {
      this.eventManager.broadcast('incomeControlListModification');
      this.activeModal.close();
    });
  }
}
