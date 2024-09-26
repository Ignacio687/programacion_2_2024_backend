import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DeviceResolve from './route/device-routing-resolve.service';

const deviceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/device.component').then(m => m.DeviceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/device-detail.component').then(m => m.DeviceDetailComponent),
    resolve: {
      device: DeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/device-update.component').then(m => m.DeviceUpdateComponent),
    resolve: {
      device: DeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/device-update.component').then(m => m.DeviceUpdateComponent),
    resolve: {
      device: DeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default deviceRoute;
