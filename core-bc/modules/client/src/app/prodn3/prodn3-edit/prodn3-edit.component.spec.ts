import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Prodn3EditComponent } from './prodn3-edit.component';

describe('Prodn3EditComponent', () => {
  let component: Prodn3EditComponent;
  let fixture: ComponentFixture<Prodn3EditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Prodn3EditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Prodn3EditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
