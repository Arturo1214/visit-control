import { IDocumentType } from 'app/shared/model/document-type.model';

export interface IVisitor {
  id?: number;
  fullName?: string;
  documentNumber?: string;
  business?: string;
  position?: string;
  imageName?: string;
  documentType?: IDocumentType;
}

export class Visitor implements IVisitor {
  constructor(
    public id?: number,
    public fullName?: string,
    public documentNumber?: string,
    public business?: string,
    public position?: string,
    public imageName?: string,
    public documentType?: IDocumentType
  ) {}
}
