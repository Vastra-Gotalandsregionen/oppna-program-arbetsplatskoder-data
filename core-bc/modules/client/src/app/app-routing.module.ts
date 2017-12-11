import {HomeComponent} from './home/home.component';
import {NgModule} from '@angular/core';
import {PreloadAllModules, RouterModule, Routes} from '@angular/router';
import {UserLoggedInGuard} from "./apk/guard/user-logged-in.guard";

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
        loadChildren: './apk/apk.module#ApkModule',
        canActivate: [UserLoggedInGuard],
      },
      {
        path: 'admin',
        loadChildren: './admin/admin.module#AdminModule',
        canActivate: [UserLoggedInGuard],
      },
      {
        path: 'report',
        loadChildren: './report/report.module#ReportModule',
        canActivate: [UserLoggedInGuard],
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
