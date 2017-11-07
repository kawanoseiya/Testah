package org.testah.runner.http.load;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.junit.Rule;
import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.runner.performance.AbstractLongRunningTest;
import org.testah.runner.performance.ChunkStatsLogPublisher;
import org.testah.runner.performance.ElasticSearchResponseTimesPublisher;
import org.testah.runner.performance.TestRunProperties;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class TestLongRunning extends AbstractLongRunningTest {
    private static final int numberOfChunks = 8;
    private static final int chunkSize = 4;
    private static final int nthreads = 2;
    private static final long millisBetweenChunks = 2000L;
    private static final long runDuration = 2000L;
    private static final String serviceUnderTest = "ServiceUnderTest";
    private static final String baseUrl = "http://localhost:9200";
    private static final String username = "elastic";
    private static final String password = "changeme";
    private static final String index = "testah";
    private static final String type = "load";
    private static final String requestRegexLineCounter = "(.*\\}\\n){10}";
    private static final String requestRegexIndexCounter = "(.*index.*\\n.*\\n){5}";
    private static final String requestRegexSingle =
                    ".*\\{\"statusCode\":%d,\"duration\":\\d+,"
                                    + "\"collectionTime\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\","
                                    + "\"domain\":\"%s\",\"service\":\"ServiceUnderTest\",\"testClass\":\"TestLongRunning\","
                                    + "\"testMethod\":\"%s\",\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\","
                                    + "\"aggregation\":\"single\"\\}.*";
    private static final String requestRegexChunk =
                    ".*\\{\"duration\":\\d+,\"domain\":\"%s\",\"service\":\"ServiceUnderTest\","
                                    + "\"testClass\":\"TestLongRunning\",\"testMethod\":\"%s\","
                                    + "\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\",\"aggregation\":\"chunk\"\\}.*";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9200);

    @Test
    @TestCase()
    public void testGet() throws Exception {
        final Integer[] expectedStatusCodes = {200, 300, 400, 500};
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
                        new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, chunkSize,
                                        numberOfChunks, millisBetweenChunks);
        ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
                        new ElasticSearchResponseTimesPublisher(baseUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        setupWiremock(elasticSearchExecutionStatsPublisher, new TestServiceGetRequestGenerator().getDomain(),
                        testMethod, expectedStatusCodes);

        executeTest(new TestServiceGetRequestGenerator(),
                        runProps
                        .setVerbose(true)
                        .setRunDuration(runDuration),
                        elasticSearchExecutionStatsPublisher,
                        chunkStatsLogPublisher);
    }

    @Test
    @TestCase()
    public void testPost() throws Exception {
        final Integer[] expectedStatusCodes = {200, 200, 200, 200};
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, chunkSize, numberOfChunks, millisBetweenChunks);
        ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
            new ElasticSearchResponseTimesPublisher(baseUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        setupWiremock(elasticSearchExecutionStatsPublisher, new TestServicePostRequestGenerator().getDomain(),
                        testMethod, expectedStatusCodes);

        executeTest(new TestServicePostRequestGenerator(),
                        runProps
                        .setVerbose(true)
                        .setRunDuration(runDuration),
                        elasticSearchExecutionStatsPublisher,
                        chunkStatsLogPublisher);
    }

    private void setupWiremock(ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher, String domain,
                    String testMethod, Integer[] expectedStatusCodes)
    {
        wireMockRule.stubFor(post(urlEqualTo(elasticSearchExecutionStatsPublisher.getUrlPathUpload()))
                        .withRequestBody(matching(requestRegexLineCounter))
                        .withRequestBody(matching(requestRegexIndexCounter))
                        .withRequestBody(matching(String.format(requestRegexSingle, expectedStatusCodes[0], domain, testMethod)))
                        .withRequestBody(matching(String.format(requestRegexSingle, expectedStatusCodes[1], domain, testMethod)))
                        .withRequestBody(matching(String.format(requestRegexSingle, expectedStatusCodes[2], domain, testMethod)))
                        .withRequestBody(matching(String.format(requestRegexSingle, expectedStatusCodes[3], domain, testMethod)))
                        .withRequestBody(matching(String.format(requestRegexChunk, domain, testMethod)))
                        .willReturn(aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("<response>Some content</response>")));
    }
}