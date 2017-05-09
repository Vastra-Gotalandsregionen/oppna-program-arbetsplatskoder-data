import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {CoreModule} from '../core/core.module';
import {SharedModule} from '../shared/shared.module';
import {AdminRoutingModule} from './admin-routing.module';
import { AdminLandingComponent } from './admin-landing/admin-landing.component';
import { AdminComponent } from './admin.component';

@NgModule({
  imports: [
    CommonModule,
    CoreModule,
    SharedModule,
    AdminRoutingModule
  ],
  declarations: [AdminLandingComponent, AdminComponent]
})
export class AdminModule { }
