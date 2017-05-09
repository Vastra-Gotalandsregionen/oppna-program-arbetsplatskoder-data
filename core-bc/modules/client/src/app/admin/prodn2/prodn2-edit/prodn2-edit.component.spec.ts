import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Prodn2EditComponent } from './prodn2-edit.component';

describe('Prodn2EditComponent', () => {
  let component: Prodn2EditComponent;
  let fixture: ComponentFixture<Prodn2EditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Prodn2EditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Prodn2EditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
