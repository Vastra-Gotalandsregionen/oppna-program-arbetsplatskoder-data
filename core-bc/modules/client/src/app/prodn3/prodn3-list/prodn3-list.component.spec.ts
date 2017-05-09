import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Prodn3ListComponent } from './prodn3-list.component';

describe('Prodn3ListComponent', () => {
  let component: Prodn3ListComponent;
  let fixture: ComponentFixture<Prodn3ListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Prodn3ListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Prodn3ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
