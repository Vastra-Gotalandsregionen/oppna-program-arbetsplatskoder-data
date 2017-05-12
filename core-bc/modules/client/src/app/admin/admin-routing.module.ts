import {NgModule} from '@angular/core';
import {AdminComponent} from './admin.component'
import {AdminLandingComponent} from './admin-landing/admin-landing.component'
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      {
        path: 'landing',
        component: AdminLandingComponent,
      },
      {
        path: 'old-users',
        loadChildren: './old-users/old-users.module#OldUsersModule'
      },
      {
        path: 'users',
        loadChildren: './users/users.module#UsersModule'
      },
      {
        path: 'prodn1',
        loadChildren: './prodn1/prodn1.module#Prodn1Module'
      },
      {
        path: 'prodn2',
        loadChildren: './prodn2/prodn2.module#Prodn2Module'
      },
      {
        path: 'prodn3',
        loadChildren: './prodn3/prodn3.module#Prodn3Module'
      },
      {
        path: 'ao3',
        loadChildren: './ao3/ao3.module#Ao3Module'
      },
      {
        path: '**',
        redirectTo: 'landing'
      },

    ]
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
