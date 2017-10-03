import { TestBed, async, inject } from '@angular/core/testing';

import { UserLoggedInGuard } from './user-logged-in.guard';

describe('UserLoggedInGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserLoggedInGuard]
    });
  });

  it('should ...', inject([UserLoggedInGuard], (guard: UserLoggedInGuard) => {
    expect(guard).toBeTruthy();
  }));
});
