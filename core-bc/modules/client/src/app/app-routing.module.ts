import {HomeComponent} from "./home/home.component";
import {NgModule} from "@angular/core";
import {PreloadAllModules, RouterModule, Routes} from "@angular/router";
import {AdminGuard} from "./core/guard/admin.guard";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path: 'apk',
        loadChildren: './apk/apk.module#ApkModule'
      },
      {
        path: 'old-users',
        loadChildren: './old-users/old-users.module#OldUsersModule'
      },
      {
        path: 'users',
        canActivate: [AdminGuard],
        loadChildren: './users/users.module#UsersModule'
      },
      {
        path: '**',
        redirectTo: ''
      }
    ]
  }

];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes, {
        preloadingStrategy: PreloadAllModules
      })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
