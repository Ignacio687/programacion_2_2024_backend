import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CustomizationResolve from './route/customization-routing-resolve.service';

const customizationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/customization.component').then(m => m.CustomizationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/customization-detail.component').then(m => m.CustomizationDetailComponent),
    resolve: {
      customization: CustomizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/customization-update.component').then(m => m.CustomizationUpdateComponent),
    resolve: {
      customization: CustomizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/customization-update.component').then(m => m.CustomizationUpdateComponent),
    resolve: {
      customization: CustomizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default customizationRoute;
