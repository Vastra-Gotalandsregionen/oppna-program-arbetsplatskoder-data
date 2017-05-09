import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OldUsersListComponent} from './old-users-list/old-users-list.component';

const routes: Routes = [
  {
    path: '',
    component: OldUsersListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OldUsersRoutingModule { }
