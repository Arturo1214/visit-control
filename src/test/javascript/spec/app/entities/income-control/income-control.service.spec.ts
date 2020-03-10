import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IncomeControlService } from 'app/entities/income-control/income-control.service';
import { IIncomeControl, IncomeControl } from 'app/shared/model/income-control.model';

describe('Service Tests', () => {
  describe('IncomeControl Service', () => {
    let injector: TestBed;
    let service: IncomeControlService;
    let httpMock: HttpTestingController;
    let elemDefault: IIncomeControl;
    let expectedResult: IIncomeControl | IIncomeControl[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(IncomeControlService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new IncomeControl(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            admissionDate: currentDate.format(DATE_TIME_FORMAT),
            departureDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a IncomeControl', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            admissionDate: currentDate.format(DATE_TIME_FORMAT),
            departureDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            admissionDate: currentDate,
            departureDate: currentDate
          },
          returnedFromService
        );

        service.create(new IncomeControl()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a IncomeControl', () => {
        const returnedFromService = Object.assign(
          {
            reason: 'BBBBBB',
            place: 'BBBBBB',
            answerable: 'BBBBBB',
            admissionDate: currentDate.format(DATE_TIME_FORMAT),
            departureDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            admissionDate: currentDate,
            departureDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of IncomeControl', () => {
        const returnedFromService = Object.assign(
          {
            reason: 'BBBBBB',
            place: 'BBBBBB',
            answerable: 'BBBBBB',
            admissionDate: currentDate.format(DATE_TIME_FORMAT),
            departureDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            admissionDate: currentDate,
            departureDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a IncomeControl', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
