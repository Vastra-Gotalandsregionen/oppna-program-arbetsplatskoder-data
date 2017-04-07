import { ApkClientPage } from './app.po';

describe('apk-client App', () => {
  let page: ApkClientPage;

  beforeEach(() => {
    page = new ApkClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
