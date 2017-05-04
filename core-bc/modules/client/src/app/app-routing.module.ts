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
        canActivate: [AdminGuard],
        loadChildren: './old-users/old-users.module#OldUsersModule'
      },
      {
        path: 'users',
        canActivate: [AdminGuard],
        loadChildren: './users/users.module#UsersModule'
      },
      {
        path: 'prodn1',
        canActivate: [AdminGuard],
        loadChildren: './prodn1/prodn1.module#Prodn1Module'
      },
      {
        path: '**',
        redirectTo: 'home'
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
