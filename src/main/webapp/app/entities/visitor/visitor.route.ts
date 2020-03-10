import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVisitor, Visitor } from 'app/shared/model/visitor.model';
import { VisitorService } from './visitor.service';
import { VisitorComponent } from './visitor.component';
import { VisitorDetailComponent } from './visitor-detail.component';
import { VisitorUpdateComponent } from './visitor-update.component';

@Injectable({ providedIn: 'root' })
export class VisitorResolve implements Resolve<IVisitor> {
  constructor(private service: VisitorService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVisitor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((visitor: HttpResponse<Visitor>) => {
          if (visitor.body) {
            return of(visitor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Visitor());
  }
}

export const visitorRoute: Routes = [
  {
    path: '',
    component: VisitorComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'visitApp.visitor.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VisitorDetailComponent,
    resolve: {
      visitor: VisitorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'visitApp.visitor.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VisitorUpdateComponent,
    resolve: {
      visitor: VisitorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'visitApp.visitor.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VisitorUpdateComponent,
    resolve: {
      visitor: VisitorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'visitApp.visitor.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
