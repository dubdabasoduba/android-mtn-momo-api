---
sidebar_position: 1
sidebar_label: Library Usage
---

# Library Usage

The MTN MOMO API SDK documentation is built using [Docusaurus](https://docusaurus.io/), a modern static website generator that simplifies the creation of documentation websites. The documentation files are organized in a structured manner within the [versioned_docs folder](https://github.com/re-kast/android-mtn-momo-api-sdk/tree/feature/issue_27/versioned_docs), with the current version being `1.0`. You can find all relevant documentation in the `version-1.0` folder.

---

## Deployment

The documentation is automatically deployed to [https://mtn-momo-sdk.rekast.io/](https://mtn-momo-sdk.rekast.io/) using [GitHub Actions](https://github.com/re-kast/android-mtn-momo-api-sdk/blob/de966147f9a240b2bdf0611663a6fd5b05cf21ae/.github/workflows/docs.yml#L20-L46). This CI/CD pipeline ensures that any updates to the documentation are promptly reflected on the website, providing users with the latest information.

### GitHub Actions Workflow

The following YAML configuration outlines the steps involved in building and deploying the Docusaurus documentation:

```yaml
build-and-deploy-docusaurus:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2.3.1
      - uses: actions/setup-node@v3
        with:
          node-version: 18
          cache: yarn

      - name: Install dependencies
        run: yarn install

      - name: Build website
        run: yarn build

      - name: Copy docs into build working-directory
        run: |
          cp -r docs ./build
          cp -r CNAME ./build

      - name: Deploy to gh-pages
        if: ${{ github.event_name == 'push' }}
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_branch: gh-pages
          publish_dir: ./build
          user_name: github-actions[bot]
          user_email: 41898282+github-actions[bot]@users.noreply.github.com
```

### Explanation of Workflow Steps

1. **Checkout the Repository**: The workflow starts by checking out the repository to access the documentation files.
2. **Setup Node.js**: It sets up Node.js version 18, which is required for building the Docusaurus site.
3. **Install Dependencies**: The workflow installs the necessary dependencies using Yarn, ensuring that all required packages are available for the build process.
4. **Build the Website**: It builds the Docusaurus site, generating static files for deployment.
5. **Copy Documentation**: The documentation files and CNAME (for custom domains) are copied into the build directory to ensure they are included in the deployment.
6. **Deploy to GitHub Pages**: Finally, the built files are deployed to the `gh-pages` branch, making them accessible via the specified URL.

## Running Documentation Locally

To preview the documentation locally before deployment, follow these steps:

1. Ensure you have Node.js and Yarn installed on your machine.
2. Navigate to the root project folder:
   ```bash
   cd path/to/your/project
   ```
3. Run the following command to build the documentation site:
   ```bash
   yarn build
   ```
4. Start a local server to serve the site:
   ```bash
   npm run serve
   ```
5. Open your web browser and navigate to `http://localhost:3000` to view the documentation.

## Additional Resources

- **Docusaurus Documentation**: For more information on how to use Docusaurus, refer to the [official documentation](https://docusaurus.io/docs).
- **GitHub Actions Documentation**: Learn more about GitHub Actions and how to automate your workflows by visiting the [GitHub Actions documentation](https://docs.github.com/en/actions).
- **Node.js Documentation**: For details on Node.js and its features, check out the [Node.js documentation](https://nodejs.org/en/docs/).
- **Yarn Documentation**: For more information on using Yarn as a package manager, visit the [Yarn documentation](https://classic.yarnpkg.com/en/docs/).

By following this documentation, developers can effectively utilize the MTN MOMO API SDK and contribute to its ongoing development and documentation efforts. This ensures that the SDK remains accessible, well-documented, and easy to integrate into applications.