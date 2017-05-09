import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Prodn2ListComponent } from './prodn2-list.component';

describe('Prodn2ListComponent', () => {
  let component: Prodn2ListComponent;
  let fixture: ComponentFixture<Prodn2ListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Prodn2ListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Prodn2ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
