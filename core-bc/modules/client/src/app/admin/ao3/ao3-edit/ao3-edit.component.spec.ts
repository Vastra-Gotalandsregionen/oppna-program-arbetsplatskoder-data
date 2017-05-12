import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Ao3EditComponent } from './ao3-edit.component';

describe('Ao3EditComponent', () => {
  let component: Ao3EditComponent;
  let fixture: ComponentFixture<Ao3EditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Ao3EditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Ao3EditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
