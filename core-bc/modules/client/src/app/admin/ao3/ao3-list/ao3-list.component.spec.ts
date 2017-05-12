import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Ao3ListComponent } from './ao3-list.component';

describe('Ao3ListComponent', () => {
  let component: Ao3ListComponent;
  let fixture: ComponentFixture<Ao3ListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Ao3ListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Ao3ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
