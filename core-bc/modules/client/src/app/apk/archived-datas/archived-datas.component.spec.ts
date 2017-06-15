import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivedDatasComponent } from './archived-datas.component';

describe('ArchivedDatasComponent', () => {
  let component: ArchivedDatasComponent;
  let fixture: ComponentFixture<ArchivedDatasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArchivedDatasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchivedDatasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
