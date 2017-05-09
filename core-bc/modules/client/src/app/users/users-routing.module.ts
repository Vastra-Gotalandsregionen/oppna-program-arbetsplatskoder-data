import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UsersListComponent} from './users-list/users-list.component';
import {UserEditComponent} from './user-edit/user-edit.component';
import {UserCreateComponent} from './user-create/user-create.component';

const routes: Routes = [
  {
    path: '',
    component: UsersListComponent,
  },
  {
    path: ':userId/edit',
    component: UserEditComponent,
  },
  {
    path: 'create',
    component: UserCreateComponent,
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
