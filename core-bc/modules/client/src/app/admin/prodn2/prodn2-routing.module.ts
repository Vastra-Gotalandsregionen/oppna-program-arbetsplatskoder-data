import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Prodn2ListComponent} from './prodn2-list/prodn2-list.component';
import {Prodn2EditComponent} from './prodn2-edit/prodn2-edit.component';

const routes: Routes = [
  {
    path: '',
    component: Prodn2ListComponent
  },
  {
    path: ':id/edit',
    component: Prodn2EditComponent
  },
  {
    path: 'create',
    component: Prodn2EditComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Prodn2RoutingModule { }
