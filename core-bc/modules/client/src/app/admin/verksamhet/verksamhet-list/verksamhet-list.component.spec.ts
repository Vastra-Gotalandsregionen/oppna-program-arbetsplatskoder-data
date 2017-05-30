import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VerksamhetListComponent } from './verksamhet-list.component';

describe('VerksamhetListComponent', () => {
  let component: VerksamhetListComponent;
  let fixture: ComponentFixture<VerksamhetListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VerksamhetListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VerksamhetListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
