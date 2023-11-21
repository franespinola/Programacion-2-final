import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'orden',
        data: { pageTitle: 'procesadorOrdenApp.orden.home.title' },
        loadChildren: () => import('./orden/orden.module').then(m => m.OrdenModule),
      },
      {
        path: 'operacion',
        data: { pageTitle: 'procesadorOrdenApp.operacion.home.title' },
        loadChildren: () => import('./operacion/operacion.module').then(m => m.OperacionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
