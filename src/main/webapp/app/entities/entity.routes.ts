import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'computechApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'device',
    data: { pageTitle: 'computechApp.device.home.title' },
    loadChildren: () => import('./device/device.routes'),
  },
  {
    path: 'characteristic',
    data: { pageTitle: 'computechApp.characteristic.home.title' },
    loadChildren: () => import('./characteristic/characteristic.routes'),
  },
  {
    path: 'customization',
    data: { pageTitle: 'computechApp.customization.home.title' },
    loadChildren: () => import('./customization/customization.routes'),
  },
  {
    path: 'option',
    data: { pageTitle: 'computechApp.option.home.title' },
    loadChildren: () => import('./option/option.routes'),
  },
  {
    path: 'extra',
    data: { pageTitle: 'computechApp.extra.home.title' },
    loadChildren: () => import('./extra/extra.routes'),
  },
  {
    path: 'sale',
    data: { pageTitle: 'computechApp.sale.home.title' },
    loadChildren: () => import('./sale/sale.routes'),
  },
  {
    path: 'sale-item',
    data: { pageTitle: 'computechApp.saleItem.home.title' },
    loadChildren: () => import('./sale-item/sale-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
