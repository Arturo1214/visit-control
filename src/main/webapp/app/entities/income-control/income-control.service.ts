import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIncomeControl, IIncomeControlDTO } from 'app/shared/model/income-control.model';

type EntityResponseType = HttpResponse<IIncomeControl>;
type EntityArrayResponseType = HttpResponse<IIncomeControl[]>;

@Injectable({ providedIn: 'root' })
export class IncomeControlService {
  public resourceUrl = SERVER_API_URL + 'api/income-controls';

  constructor(protected http: HttpClient) {}

  create(incomeControl: IIncomeControlDTO): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeControl);
    return this.http
      .post<IIncomeControl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(incomeControl: IIncomeControlDTO): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeControl);
    return this.http
      .put<IIncomeControl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIncomeControl>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIncomeControl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  exit(id: number): Observable<HttpResponse<{}>> {
    return this.http.put(`${this.resourceUrl}/exit/${id}`, null, { observe: 'response' });
  }

  protected convertDateFromClient(incomeControl: IIncomeControl): IIncomeControl {
    const copy: IIncomeControl = Object.assign({}, incomeControl, {
      admissionDate:
        incomeControl.admissionDate && incomeControl.admissionDate.isValid() ? incomeControl.admissionDate.toJSON() : undefined,
      departureDate: incomeControl.departureDate && incomeControl.departureDate.isValid() ? incomeControl.departureDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.admissionDate = res.body.admissionDate ? moment(res.body.admissionDate) : undefined;
      res.body.departureDate = res.body.departureDate ? moment(res.body.departureDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((incomeControl: IIncomeControl) => {
        incomeControl.admissionDate = incomeControl.admissionDate ? moment(incomeControl.admissionDate) : undefined;
        incomeControl.departureDate = incomeControl.departureDate ? moment(incomeControl.departureDate) : undefined;
      });
    }
    return res;
  }
}
