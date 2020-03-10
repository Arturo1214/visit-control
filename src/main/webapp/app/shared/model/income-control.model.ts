import { Moment } from 'moment';
import { IVisitor } from 'app/shared/model/visitor.model';

export interface IIncomeControlDTO {
  id?: number;
  reason?: string;
  place?: string;
  answerable?: string;
  visitor?: IVisitor;
}

export class IncomeControlDTO implements IIncomeControlDTO {
  constructor(public id?: number, public reason?: string, public place?: string, public answerable?: string, public visitor?: IVisitor) {}
}

export interface IIncomeControl {
  id?: number;
  reason?: string;
  place?: string;
  answerable?: string;
  admissionDate?: Moment;
  departureDate?: Moment;
  visitor?: IVisitor;
}

export class IncomeControl implements IIncomeControl {
  constructor(
    public id?: number,
    public reason?: string,
    public place?: string,
    public answerable?: string,
    public admissionDate?: Moment,
    public departureDate?: Moment,
    public visitor?: IVisitor
  ) {}
}
