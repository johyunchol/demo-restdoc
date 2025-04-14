'use strict';
const localStorageKeys = {
    'indexVersion': 'index-version-admin',
    'docs': 'docs-admin'
};
const parser = new DOMParser();
const searchScriptElement = document.getElementById('search-script');
const basePath = searchScriptElement.dataset['basePath'];
let index;

function indexDocs(docs) {
    index = new Fuse(docs, {keys: ['title']});
    window.search.init(index);
}

const cachedCommitHash = localStorage.getItem(localStorageKeys.indexVersion);
const latestCommitHash = searchScriptElement.dataset['commitSha'];
if (latestCommitHash && cachedCommitHash === latestCommitHash) {
    console.log('oh! you already have valid docs! I am gonna use cached docs!');
    const cachedDocsJson = localStorage.getItem(localStorageKeys.docs);
    const cachedDocs = JSON.parse(cachedDocsJson);
    indexDocs(cachedDocs);
} else {
    console.log(`Oops! cached docs are out-dated. I am gonna fetch new docs from the server! yours - ${cachedCommitHash}, new - ${latestCommitHash}`);
    localStorage.setItem(localStorageKeys.indexVersion, latestCommitHash);
    fetch(`${basePath}/api-admin/sitemap.xml`)
        .then(res => res.text())
        .then(textBody => {
            const doc = parser.parseFromString(textBody, 'application/xml');
            const urlElements = Array.from(doc.children.item(0).children);
            return urlElements.map(element => element.children.item(0).textContent);
        })
        .then(urls => Promise.all(urls.map(url => fetch(url)
            .then(res => res.text())
            .then(textBody => parser.parseFromString(textBody, 'text/html'))
            .then(doc => ({'url': url, 'document': doc})))))
        .then(docs => docs.map(doc => Array.from(doc.document.querySelectorAll('article.doc .sect1 > h2 > .anchor'))
            .map(element => ({
                'title': element.parentElement.innerText,
                'url': `${doc.url}${element.getAttribute('href')}`
            })))
            .reduce(((previousValue, currentValue) => previousValue.concat(currentValue)), [])
        ).then(docs => {
        localStorage.setItem(localStorageKeys.docs, JSON.stringify(docs));
        indexDocs(docs);
    });
}
