import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Prodn1ListComponent} from './prodn1-list/prodn1-list.component';
import {Prodn1EditComponent} from './prodn1-edit/prodn1-edit.component';

const routes: Routes = [
  {
    path: '',
    component: Prodn1ListComponent
  },
  {
    path: ':id/edit',
    component: Prodn1EditComponent
  },
  {
    path: 'create',
    component: Prodn1EditComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Prodn1RoutingModule { }
