import { TestBed, async, inject } from '@angular/core/testing';

import { FormChangedGuard } from './form-changed.guard';

describe('FormChangedGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FormChangedGuard]
    });
  });

  it('should ...', inject([FormChangedGuard], (guard: FormChangedGuard) => {
    expect(guard).toBeTruthy();
  }));
});
