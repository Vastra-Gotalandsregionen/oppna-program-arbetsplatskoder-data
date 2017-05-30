import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VerksamhetEditComponent } from './verksamhet-edit.component';

describe('VerksamhetEditComponent', () => {
  let component: VerksamhetEditComponent;
  let fixture: ComponentFixture<VerksamhetEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VerksamhetEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VerksamhetEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
