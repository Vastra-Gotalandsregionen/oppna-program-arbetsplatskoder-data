import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApkCreateComponent } from './apk-create.component';

describe('ApkCreateComponent', () => {
  let component: ApkCreateComponent;
  let fixture: ComponentFixture<ApkCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApkCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApkCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
