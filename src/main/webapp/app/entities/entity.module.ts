import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'document-type',
        loadChildren: () => import('./document-type/document-type.module').then(m => m.VisitDocumentTypeModule)
      },
      {
        path: 'visitor',
        loadChildren: () => import('./visitor/visitor.module').then(m => m.VisitVisitorModule)
      },
      {
        path: 'income-control',
        loadChildren: () => import('./income-control/income-control.module').then(m => m.VisitIncomeControlModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class VisitEntityModule {}
