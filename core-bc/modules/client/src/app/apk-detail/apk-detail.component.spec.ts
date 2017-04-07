import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApkDetailComponent } from './apk-detail.component';

describe('ApkDetailComponent', () => {
  let component: ApkDetailComponent;
  let fixture: ComponentFixture<ApkDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApkDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApkDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
