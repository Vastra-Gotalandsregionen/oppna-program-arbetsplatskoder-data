import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SidenavToggleButtonComponent } from './sidenav-toggle-button.component';

describe('SidenavToggleButtonComponent', () => {
  let component: SidenavToggleButtonComponent;
  let fixture: ComponentFixture<SidenavToggleButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SidenavToggleButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavToggleButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
