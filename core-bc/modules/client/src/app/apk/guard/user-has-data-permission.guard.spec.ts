import { TestBed, async, inject } from '@angular/core/testing';

import { UserHasDataPermissionGuard } from './user-has-data-permission.guard';

describe('UserHasDataPermissionGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserHasDataPermissionGuard]
    });
  });

  it('should ...', inject([UserHasDataPermissionGuard], (guard: UserHasDataPermissionGuard) => {
    expect(guard).toBeTruthy();
  }));
});
