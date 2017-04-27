import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {OldUsersListComponent} from './old-users-list.component';

describe('UsersListComponent', () => {
  let component: OldUsersListComponent;
  let fixture: ComponentFixture<OldUsersListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OldUsersListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OldUsersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
