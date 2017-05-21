import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VardformListComponent } from './vardform-list.component';

describe('VardformListComponent', () => {
  let component: VardformListComponent;
  let fixture: ComponentFixture<VardformListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VardformListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VardformListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
