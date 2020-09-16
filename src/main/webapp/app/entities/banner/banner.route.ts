import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBanner, Banner } from 'app/shared/model/banner.model';
import { BannerService } from './banner.service';
import { BannerComponent } from './banner.component';
import { BannerDetailComponent } from './banner-detail.component';
import { BannerUpdateComponent } from './banner-update.component';

@Injectable({ providedIn: 'root' })
export class BannerResolve implements Resolve<IBanner> {
  constructor(private service: BannerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBanner> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((banner: HttpResponse<Banner>) => {
          if (banner.body) {
            return of(banner.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Banner());
  }
}

export const bannerRoute: Routes = [
  {
    path: '',
    component: BannerComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'convidApp.banner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BannerDetailComponent,
    resolve: {
      banner: BannerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'convidApp.banner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BannerUpdateComponent,
    resolve: {
      banner: BannerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'convidApp.banner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BannerUpdateComponent,
    resolve: {
      banner: BannerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'convidApp.banner.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
