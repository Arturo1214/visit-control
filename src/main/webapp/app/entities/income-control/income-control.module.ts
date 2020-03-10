import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { VisitSharedModule } from 'app/shared/shared.module';
import { IncomeControlComponent } from './income-control.component';
import { IncomeControlDetailComponent } from './income-control-detail.component';
import { IncomeControlUpdateComponent } from './income-control-update.component';
import { IncomeControlDeleteDialogComponent } from './income-control-delete-dialog.component';
import { incomeControlRoute } from './income-control.route';
import { WebcamModule } from 'ngx-webcam';
import { IncomeControlExitDialogComponent } from 'app/entities/income-control/income-control-exit-dialog.component';

@NgModule({
  imports: [WebcamModule, VisitSharedModule, RouterModule.forChild(incomeControlRoute)],
  declarations: [
    IncomeControlComponent,
    IncomeControlDetailComponent,
    IncomeControlUpdateComponent,
    IncomeControlDeleteDialogComponent,
    IncomeControlExitDialogComponent
  ],
  entryComponents: [IncomeControlDeleteDialogComponent, IncomeControlExitDialogComponent]
})
export class VisitIncomeControlModule {}
