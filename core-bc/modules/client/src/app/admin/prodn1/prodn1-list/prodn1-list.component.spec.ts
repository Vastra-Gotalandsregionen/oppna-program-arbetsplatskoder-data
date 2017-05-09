import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Prodn1ListComponent } from './prodn1-list.component';

describe('Prodn1ListComponent', () => {
  let component: Prodn1ListComponent;
  let fixture: ComponentFixture<Prodn1ListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Prodn1ListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Prodn1ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
