import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VardformEditComponent } from './vardform-edit.component';

describe('VardformEditComponent', () => {
  let component: VardformEditComponent;
  let fixture: ComponentFixture<VardformEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VardformEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VardformEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
