import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { VisitTestModule } from '../../../test.module';
import { IncomeControlUpdateComponent } from 'app/entities/income-control/income-control-update.component';
import { IncomeControlService } from 'app/entities/income-control/income-control.service';
import { IncomeControl } from 'app/shared/model/income-control.model';

describe('Component Tests', () => {
  describe('IncomeControl Management Update Component', () => {
    let comp: IncomeControlUpdateComponent;
    let fixture: ComponentFixture<IncomeControlUpdateComponent>;
    let service: IncomeControlService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [VisitTestModule],
        declarations: [IncomeControlUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IncomeControlUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IncomeControlUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IncomeControlService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IncomeControl(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new IncomeControl();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
