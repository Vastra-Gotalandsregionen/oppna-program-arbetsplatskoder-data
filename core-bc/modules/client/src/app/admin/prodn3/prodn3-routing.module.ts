import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Prodn3ListComponent} from './prodn3-list/prodn3-list.component';

const routes: Routes = [
  {
    path: '',
    component: Prodn3ListComponent
  },
  /*{
    path: ':producentid/edit',
    component: Prodn2EditComponent
  },
  {
    path: 'create',
    component: Prodn2EditComponent
  }*/
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Prodn3RoutingModule { }
