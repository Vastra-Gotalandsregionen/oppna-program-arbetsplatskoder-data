import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApkEditComponent } from './apk-edit.component';

describe('ApkEditComponent', () => {
  let component: ApkEditComponent;
  let fixture: ComponentFixture<ApkEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApkEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApkEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
