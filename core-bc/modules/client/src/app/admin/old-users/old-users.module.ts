import {NgModule} from '@angular/core';
import {SharedModule} from '../../shared/shared.module';
import {OldUsersRoutingModule} from './old-users-routing.module';
import {OldUsersListComponent} from './old-users-list/old-users-list.component';

@NgModule({
  imports: [
    OldUsersRoutingModule,
    SharedModule
  ],
  declarations: [OldUsersListComponent]
})
export class OldUsersModule { }
