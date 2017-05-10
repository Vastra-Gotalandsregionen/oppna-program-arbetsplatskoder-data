import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Prodn1EditComponent } from './prodn1-edit.component';

describe('Prodn1EditComponent', () => {
  let component: Prodn1EditComponent;
  let fixture: ComponentFixture<Prodn1EditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Prodn1EditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Prodn1EditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
