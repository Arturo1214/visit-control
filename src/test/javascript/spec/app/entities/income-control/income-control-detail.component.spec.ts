import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VisitTestModule } from '../../../test.module';
import { IncomeControlDetailComponent } from 'app/entities/income-control/income-control-detail.component';
import { IncomeControl } from 'app/shared/model/income-control.model';

describe('Component Tests', () => {
  describe('IncomeControl Management Detail Component', () => {
    let comp: IncomeControlDetailComponent;
    let fixture: ComponentFixture<IncomeControlDetailComponent>;
    const route = ({ data: of({ incomeControl: new IncomeControl(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VisitTestModule],
        declarations: [IncomeControlDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IncomeControlDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IncomeControlDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load incomeControl on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.incomeControl).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
