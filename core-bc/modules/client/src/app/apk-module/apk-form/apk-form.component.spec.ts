import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApkFormComponent } from './apk-form.component';

describe('ApkFormComponent', () => {
  let component: ApkFormComponent;
  let fixture: ComponentFixture<ApkFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApkFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApkFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
