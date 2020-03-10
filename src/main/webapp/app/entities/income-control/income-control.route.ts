import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IIncomeControl, IncomeControl } from 'app/shared/model/income-control.model';
import { IncomeControlService } from './income-control.service';
import { IncomeControlComponent } from './income-control.component';
import { IncomeControlDetailComponent } from './income-control-detail.component';
import { IncomeControlUpdateComponent } from './income-control-update.component';

@Injectable({ providedIn: 'root' })
export class IncomeControlResolve implements Resolve<IIncomeControl> {
  constructor(private service: IncomeControlService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncomeControl> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((incomeControl: HttpResponse<IncomeControl>) => {
          if (incomeControl.body) {
            return of(incomeControl.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IncomeControl());
  }
}

export const incomeControlRoute: Routes = [
  {
    path: '',
    component: IncomeControlComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'visitApp.incomeControl.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IncomeControlDetailComponent,
    resolve: {
      incomeControl: IncomeControlResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'visitApp.incomeControl.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IncomeControlUpdateComponent,
    resolve: {
      incomeControl: IncomeControlResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'visitApp.incomeControl.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IncomeControlUpdateComponent,
    resolve: {
      incomeControl: IncomeControlResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'visitApp.incomeControl.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
